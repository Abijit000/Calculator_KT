package com.example.calculatorkt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity()
{
    private var canAddOperation= false
    private var canAddDecimal= false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberAction(view: View)
    {
        if (view is Button)
        {
            if (canAddDecimal)
            {
                workingsTV.append(view.text)
                canAddDecimal=false
            }
            else
            {
                workingsTV.append(view.text)
            }

            if (view.text[0]!='.')
            {
                canAddOperation = true
            }
        }
    }
    fun operationAction(view: View)
    {
        if (view is Button && canAddOperation)
        {
            workingsTV.append(view.text)
            canAddOperation= false
            canAddDecimal = true

        }
    }
    fun backSpaceAction(view: View)
    {
        val length =workingsTV.length()
        if (length>0)
        {
            workingsTV.text = workingsTV.text.subSequence(0,length-1)
        }
        if (workingsTV.length()==0)
        {
            canAddDecimal=false
            canAddOperation=false
        }
    }
    fun allClearAction(view: View)
    {
        workingsTV.text= ""
        resultsTV.text= ""
        canAddDecimal=false
        canAddOperation=false
    }
    fun equalsAction(view: View)
    {
        resultsTV.text =calculateResult()
    }


    private fun calculateResult(): CharSequence?
    {
        val digitsOperators = digitsOperators()
        if (digitsOperators.isEmpty()) return ""

        val mulDiv = mulDivCalculation(digitsOperators)
        if (mulDiv.isEmpty()) return ""
        
        val result = addSubCalculation(mulDiv)
        return result.toString()
    }

    private fun addSubCalculation(passedList: MutableList<Any>): Float
    {
        var result = passedList[0] as Float

        for (i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator =passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator =='+')
                {
                    result += nextDigit
                }
                if (operator =='-')
                {
                    result -= nextDigit
                }
            }
        }
        return result

    }

    private fun mulDivCalculation(passedList: MutableList<Any>): MutableList<Any>
    {
        var  list = passedList
        while (list.contains('x') || list.contains('/'))
        {
            list = calcMulDiv(list)
        }
        return list
    }

    private fun calcMulDiv(passedList: MutableList<Any>): MutableList<Any>
    {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices)
        {

            if (passedList[i] is Char && i!= passedList.lastIndex && i< restartIndex){
                val operator =passedList[i]
                val prevDigit = passedList[i-1] as Float
                val nextDigit = passedList[i+1] as Float

                when(operator)
                {
                    'x' ->
                    {
                        newList.add(prevDigit*nextDigit)
                        restartIndex = i+1

                    }
                    '/' ->
                    {
                        newList.add(prevDigit/nextDigit)
                        restartIndex = i+1

                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if (i>restartIndex)
                newList.add(passedList[i])

        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (charecter in workingsTV.text)
        {
            if (charecter.isDigit() || charecter == '.')
            {
                currentDigit += charecter
            }
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(charecter)
            }
        }

        if (currentDigit != ""){
            list.add(currentDigit.toFloat())
        }
        return list
    }
}