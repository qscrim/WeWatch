package com.example.wewatch.domain.usecase

abstract class UseCase<out Type, in Params> {
    abstract suspend fun execute(params: Params): Type

    class None
}