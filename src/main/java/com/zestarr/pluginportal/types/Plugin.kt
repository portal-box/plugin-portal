package com.zestarr.pluginportal.types

import lombok.Data

@Data
class Plugin {
    private val fileName // Optional
            : String? = null
    private val displayName: String? = null
    private val version: String? = null
    private val description: String? = null
    private val downloadLink: String? = null
    private val sha256: String? = null
    private val dependencies // Optional
            : Array<String>
    private val recommendedPlugins // Optional
            : Array<String>
}