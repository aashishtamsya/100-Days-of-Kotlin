package com.aashishtamsya.android.onlinetictactoe.models

import android.graphics.Color

enum class PlayerType {
    CROSS {
        override fun value(): Int {
            return 2
        }

        override fun playerName(): String {
            return "CROSS"
        }

        override fun symbol(): String {
            return "X"
        }

        override fun getBackgroundColor(): Int {
            return Color.CYAN
        }
    },

    NOUGHT {
        override fun value(): Int {
            return 1
        }

        override fun playerName(): String {
            return "NOUGHT"
        }

        override fun symbol(): String {
            return "O"
        }
        override fun getBackgroundColor(): Int {
            return Color.GREEN
        }
    };

    abstract fun value(): Int
    abstract fun playerName(): String
    abstract fun symbol(): String
    abstract fun getBackgroundColor(): Int
}