package com.dotnet.nikit.investingmonitor.mappers

interface IMapper<I, O> {
    fun map(input: I): O
}