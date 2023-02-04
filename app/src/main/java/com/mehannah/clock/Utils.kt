package com.mehannah.clock

class Utils {

    companion object {
        fun getSize(name: String): Int {
            if (name == "Large") return 96;
            if (name == "Medium") return 48;
            return 24;
        }

        fun getSizeIndex(name: String): Int {
            if (name == "Large") return 2;
            if (name == "Medium") return 1;
            return 0;
        }
    }
}