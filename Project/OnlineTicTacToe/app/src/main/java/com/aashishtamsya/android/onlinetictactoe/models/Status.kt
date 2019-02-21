package com.aashishtamsya.android.onlinetictactoe.models

enum class Status {
    REQUESTED {
        override fun value(): String {
            return "requested"
        }
    },
    ACCEPTED {
        override fun value(): String {
            return "accepted"
        }
    };

    abstract fun value(): String
}