package com.aashishtamsya.android.onlinetictactoe.models

import com.aashishtamsya.android.onlinetictactoe.interfaces.Encoder

class Game {
    companion object {
        val KEY_ACTIVE_PLAYER = "active_player"
        val KEY_STATUS = "status"
        val KEY_BUTTON_0 = "0"
        val KEY_BUTTON_1 = "1"
        val KEY_BUTTON_2 = "2"
        val KEY_BUTTON_3 = "3"
        val KEY_BUTTON_4 = "4"
        val KEY_BUTTON_5 = "5"
        val KEY_BUTTON_6 = "6"
        val KEY_BUTTON_7 = "7"
        val KEY_BUTTON_8 = "8"

        fun decode(data: Map<String, Any>?): Game? {
            if (data == null) return null
            val activePlayer = data[KEY_ACTIVE_PLAYER] as? String
            val _status = data[KEY_STATUS] as? String
            val button0 = data[KEY_BUTTON_0] as? String
            val button1 = data[KEY_BUTTON_1] as? String
            val button2 = data[KEY_BUTTON_2] as? String
            val button3 = data[KEY_BUTTON_3] as? String
            val button4 = data[KEY_BUTTON_4] as? String
            val button5 = data[KEY_BUTTON_5] as? String
            val button6 = data[KEY_BUTTON_6] as? String
            val button7 = data[KEY_BUTTON_7] as? String
            val button8 = data[KEY_BUTTON_8] as? String
            return Game(activePlayer, _status, button0, button1, button2, button3, button4, button5, button6, button7, button8)
        }
    }
    var activePlayer: String? = null
    private var _status: String? = null
    val status: Status? get() {
        if (_status == null) return null
        if (_status!!.equals(Status.ACCEPTED)) {
            return Status.ACCEPTED
        } else if (_status!!.equals(Status.REQUESTED)) {
            return Status.REQUESTED
        } else {
            return null
        }
    }
    var button0: String? = null
    var button1: String? = null
    var button2: String? = null
    var button3: String? = null
    var button4: String? = null
    var button5: String? = null
    var button6: String? = null
    var button7: String? = null
    var button8: String? = null

    constructor(activePlayer: String? = null,
                _status: String? = null,
                button0: String? = null,
                button1: String? = null,
                button2: String? = null,
                button3: String? = null,
                button4: String? = null,
                button5: String? = null,
                button6: String? = null,
                button7: String? = null,
                button8: String? = null) {
        this.activePlayer = activePlayer
        this._status = _status
        this.button0 = button0
        this.button1 = button1
        this.button2 = button2
        this.button3 = button3
        this.button4 = button4
        this.button5 = button5
        this.button6 = button6
        this.button7 = button7
        this.button8 = button8
    }

//    override fun encode(): HashMap<String, Any> {
//        val hashMap = HashMap<String, Any>()
//        state?.let {
//            hashMap[KEY_STATE] = it
//        }
//        activePlayer?.let {
//            hashMap[KEY_ACTIVE_PLAYER] = it
//        }
//        return hashMap
//    }
}