package com.mrkenhoff.qalculate.ui

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.mrkenhoff.qalculate.R
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Element
import org.w3c.dom.Node
import javax.xml.parsers.DocumentBuilderFactory
import org.greenrobot.eventbus.EventBus


/**
 * A [Fragment] subclass that shows a button layout.
 */
@AndroidEntryPoint
class ButtonLayoutFragment : Fragment() {

    companion object {
        const val ARG_FILENAME = "filename"

        fun newInstance(filename: String): ButtonLayoutFragment {
            val args = Bundle()
            args.putString(ARG_FILENAME, filename)
            val fragment = ButtonLayoutFragment ()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val stream = requireContext().assets.open(arguments?.getString(ARG_FILENAME, "")!!)
        val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        return buildView(docBuilder.parse(stream).documentElement)
    }

    private fun Element.getAttribute(name: String, default: String) : String {
        return if (hasAttribute(name)) getAttribute(name) else default
    }

    private val Number.toPx get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(), Resources.getSystem().displayMetrics)

    private fun dispatchButtonAction(action: String) {
        if (action == "@backspace@") {
            EventBus.getDefault().post(BackspaceEvent())
        } else if (action == "@clear@") {
            EventBus.getDefault().post(ClearEvent())
        } else {
            val textSegments = action.split("@")
            if (textSegments.size == 1) {
                EventBus.getDefault().post(TypeEvent(textSegments[0], ""))
            } else if (textSegments.size == 2) {
                EventBus.getDefault().post(TypeEvent(textSegments[0], textSegments[1]))
            } else {
                // TODO: Print Warning
            }
        }
    }

    private fun buildView(element: Element): View {
        when (element.nodeName) {
            "button" -> {
                val style = when (element.getAttribute("style")) {
                    "outlined" ->R.attr.calculatorButtonOutlined
                    else ->R.attr.calculatorButtonDefault
                }

                val button = CalculatorButton(requireContext(), null)
                button.text = element.getAttribute("text")

                button.topLeftText = element.getAttribute("topLeftText", "")
                button.topRightText = element.getAttribute("topRightText", "")
                button.bottomRightText = element.getAttribute("bottomRightText", "")
                button.bottomLeftText = element.getAttribute("bottomLeftText", "")

                val cAction = element.getAttribute("action", element.getAttribute("text"))
                button.onCenterAction = { dispatchButtonAction(cAction) }

                val clAction = element.getAttribute("longPressAction")
                button.onLongCenterAction = { dispatchButtonAction(clAction) }

                val tlAction = element.getAttribute("topLeftAction", element.getAttribute("topLeftText"))
                button.onTopLeftAction = { dispatchButtonAction(tlAction) }

                val trAction = element.getAttribute("topRightAction", element.getAttribute("topRightText"))
                button.onTopRightAction = { dispatchButtonAction(trAction) }

                val brAction = element.getAttribute("bottomRightAction", element.getAttribute("bottomRightText"))
                button.onBottomRightAction = { dispatchButtonAction(brAction) }

                val blAction = element.getAttribute("bottomLeftAction", element.getAttribute("bottomLeftText"))
                button.onBottomLeftAction = { dispatchButtonAction(blAction) }

                return button
            }
            "space" -> {
                return View(context)
            }
            "include" -> {
                val stream = requireContext().assets.open(element.getAttribute("src"))
                val docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                return buildView(docBuilder.parse(stream).documentElement)
            }
            "horizontal", "vertical" -> {
                val horizontal = element.nodeName == "horizontal"
                val group = LinearLayout(requireContext())
                group.orientation = if (horizontal) LinearLayout.HORIZONTAL else LinearLayout.VERTICAL
                for (i in 0 until element.childNodes.length) {
                    val child = element.childNodes.item(i)
                    if (child.nodeType == Node.ELEMENT_NODE) {
                        val childView = buildView(child as Element)
                        val layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
                        )
                        layoutParams.weight = 1 / child.getAttribute("weight", "1").toFloat()
                        childView.layoutParams = layoutParams
                        group.addView(childView)
                    }
                }
                return group
            }
            else -> {
                throw java.lang.IllegalArgumentException("Invalid element: " + element.nodeName)
            }
        }
    }
}