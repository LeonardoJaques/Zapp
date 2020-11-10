package com.jaques.projetos.zapp.helper

import android.os.Build
import androidx.annotation.RequiresApi

/** author Leonardo Jaques on 05/11/20 */


class Base64Custom {
    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        fun codeBase64(text: String): String =
            java.util.Base64
                .getEncoder()
                .withoutPadding()
                .encodeToString(
                    text.replace("\\n\\r", "")
                        .toByteArray()
                )

        @RequiresApi(Build.VERSION_CODES.O)
        fun decodeBase64(textCode: String): String =
            java.util.Base64
                .getDecoder()
                .decode(
                    textCode.replace("\\n\\r", "")
                        .toByteArray()
                )
                .toString()
    }
}


