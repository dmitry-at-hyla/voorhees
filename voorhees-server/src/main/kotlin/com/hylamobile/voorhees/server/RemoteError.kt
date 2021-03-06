package com.hylamobile.voorhees.server

import com.hylamobile.voorhees.jsonrpc.Error
import com.hylamobile.voorhees.jsonrpc.Request
import com.hylamobile.voorhees.jsonrpc.Response

class RemoteError(
    private val error: Error,
    private val config: RemoteConfig) : RemoteMethod {

    override val notificationExecutor: NotificationExecutor
        get() = config.notificationExecutor

    override fun invoke(request: Request): Response<*> =
        Response.error(error, request.id)
}
