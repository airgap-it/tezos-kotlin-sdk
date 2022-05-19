package it.airgap.tezos.contract.exception

public class ContractException(message: String? = null, cause: Throwable? = null) : Exception(message, cause)