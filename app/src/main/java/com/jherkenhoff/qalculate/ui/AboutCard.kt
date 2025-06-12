package com.jherkenhoff.qalculate.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jherkenhoff.libqalculate.libqalculateConstants;
import com.jherkenhoff.qalculate.BuildConfig
import com.jherkenhoff.qalculate.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutCard(
    modifier: Modifier = Modifier
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
    ) {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodySmall) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp, horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Qalculate logo",
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(6.dp, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = "Qalculate!", style = MaterialTheme.typography.headlineSmall)
                        Text(
                            text = "App version " + BuildConfig.VERSION_NAME
                        )
                        Text(
                            text = "libqalculate version ${libqalculateConstants.QALCULATE_MAJOR_VERSION}" +
                                    ".${libqalculateConstants.QALCULATE_MINOR_VERSION}" +
                                    ".${libqalculateConstants.QALCULATE_MICRO_VERSION}"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = stringResource(R.string.about_qalculate_text),
                    textAlign = TextAlign.Center
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 20.dp))
                Text(text = "Copyright © 2023 - 2024 Jost Herkenhoff")
                Spacer(modifier = Modifier.height(6.dp))
                LicenseText()
            }
        }
    }
}

@Composable
fun LicenseText() {

    // Display a link in the text
    Text(
        buildAnnotatedString {
            append("This program comes with absolutely no warranty. See the ")
            withLink(
                LinkAnnotation.Url(
                    "https://www.gnu.org/licenses/old-licenses/gpl-2.0.html",
                    TextLinkStyles(style = SpanStyle(textDecoration = TextDecoration.Underline))
                )
            ) {
                append("GNU General Public License, version 2 or later")
            }
            append(" for details.")
        },
        textAlign = TextAlign.Center
    )

}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    AboutCard()
}
