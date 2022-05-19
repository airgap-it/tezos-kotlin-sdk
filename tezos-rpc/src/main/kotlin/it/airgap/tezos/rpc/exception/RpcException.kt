package it.airgap.tezos.rpc.exception

public class RpcException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)