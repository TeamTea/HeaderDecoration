package ir.teamtea.headersample.data

import ir.teamtea.headersample.model.Item
import ir.teamtea.headersample.model.UiItems

class FakeData(){

    companion object{
        val data = mutableListOf<UiItems>()
        fun createData() : List<UiItems>{
            for (i in 1..10){
            for (j in 1..50){
                data.add(UiItems.XItems(Item("Text$j")))
            }
                data.add(UiItems.XSeparator("separator$i"))
            }
            return data
        }
    }
}