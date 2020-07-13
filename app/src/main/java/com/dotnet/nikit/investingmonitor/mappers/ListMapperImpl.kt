package com.dotnet.nikit.investingmonitor.mappers

class ListMapperImpl<I, O>(
    private val mapper: IMapper<I, O>
) : IListMapper<I, O> {
    override fun map(input: List<I>): List<O> {
        return input.map { mapper.map(it) }
    }
}