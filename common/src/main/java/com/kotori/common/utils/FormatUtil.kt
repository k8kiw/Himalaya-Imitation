package com.kotori.common.utils


// 声音长度：秒转化为分钟
fun String.formatDuration() : String{
    // 拿到整型时间
    val duration = this.toLong()
    // 计算
    val minute = duration / 60
    val second = duration % 60
    // 补零
    val min = if (minute < 10) {
        "0${minute}"
    } else {
        "$minute"
    }
    val sec = if (second < 10) {
        "0${second}"
    } else {
        "$second"
    }
    // 拼接
    return "$min:$sec"
}


// 播放量、订阅量，进行单位转换
fun String.formatNum() : String {
    val num = this.toLong()
    // 1w 到 9999.9w
    return when {
        num < 10000 -> {
            // 无单位
            num.toString()
        }
        num >= 100000000 -> {
            // 亿单位
            val left = num / 100000000
            val right = num % 100000000
            "$left.${right.toString()[0]}亿"
        }
        else -> {
            // 万单位
            val left = num / 10000
            val right = num % 10000
            "$left.${right.toString()[0]}万"
        }
    }
}


// 删去title的多余部分 | 之后去掉
fun String.trimAlbumTitle() : String {
    val result = this.split("|", "｜", "丨")
    result.forEach {
        LogUtil.d("utils test", it)
    }
    return result[0]
}