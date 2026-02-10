package com.viewnext.domain.repository

/**
 * Callback genérico para operaciones asincrónicas que retornan un resultado o un error.
 * @param <T> Tipo de resultado esperado en caso de éxito.
</T> */
interface DetallesCallback<T> {
    fun onSuccess(result: T)
    fun onFailure(error: Throwable)
}