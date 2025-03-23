package io.justdevit.telegram.flow.dsl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.nulls.shouldNotBeNull

class ChatFlowDslTest :
    FreeSpec(
        {
            "Should be able to create chat flow" {
                val flow = chatFlow("TEST") {
                    "hello" {
                        message { "World!" }
                    }

                    "payment" {
                        sendInvoice(
                            identifier = "123",
                            title = "Hello",
                            description = "World",
                            price = 123.toBigDecimal(),
                            currency = "CZK",
                            providerToken = "TOKEN",
                        )
                    } awaitPayment {
                        message { "Payment received!" }
                    }
                }

                flow.shouldNotBeNull()
            }
        },
    )
