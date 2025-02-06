package io.justdevit.telegram.flow.i18n

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class TextsTest :
    FreeSpec(
        {
            "Should be able to convert emoji by alias" {
                T("test.hello") shouldBe """👋 Hello!"""
            }
        },
    )
