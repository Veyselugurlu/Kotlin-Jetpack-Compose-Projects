package com.example.buildatipcalculatorapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.buildatipcalculatorapp.components.InputField
import com.example.buildatipcalculatorapp.ui.theme.BuildATipCalculatorAppTheme
import com.example.buildatipcalculatorapp.util.calculateTotalPerPerson
import com.example.buildatipcalculatorapp.util.calculateTotalTip
import com.example.buildatipcalculatorapp.widget.RoundIconButton
import kotlin.math.absoluteValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
                MyApp(){
                   MainContent()

                }

        }
    }
}
@Composable
fun MyApp(content:@Composable () ->Unit){
    BuildATipCalculatorAppTheme {
        Surface(modifier=Modifier.fillMaxWidth()
        ) {
            content()
        }
    }
}

//@Preview
@Composable
fun TopHeader(totalPerPerson: Double = 134.0){
    Surface(modifier=Modifier
        .fillMaxWidth()
        .height(180.dp)
        .padding(15.dp)
        .clip(shape = RoundedCornerShape(
            corner = CornerSize(10.dp))),
        color = Color(0xFFE9D7F7))
        {
            Column(modifier=Modifier.padding(13.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center

            ){
                val total="%.2f".format(totalPerPerson)
                Text("Total per Person",
                    style = MaterialTheme.typography.headlineLarge)
                Text("$$total",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold
                )
            }
    }
}

@Preview
@Composable
fun MainContent(){
    val splitByState= remember {
            mutableStateOf(1)
        }
       // val range= IntRange(start = 1, endInclusive = 100)

        val tipAmountState= remember {
            mutableStateOf(0.0)
        }
        val totalPerPersonState= remember {
            mutableStateOf(0.0)
        }


    Column(modifier=Modifier.padding(all=12.dp)){
        BillForm(splitByState = splitByState,

                tipAmountState = tipAmountState,
            totalPerPersonState = totalPerPersonState
            ) { billAmt ->
            Log.d("AMT", "Main Content $billAmt")
        }
    }
}

@Composable
fun BillForm(modifier: Modifier = Modifier,
             range: IntRange=1..100,
             splitByState: MutableState<Int>,
             tipAmountState: MutableState<Double>,
             totalPerPersonState: MutableState<Double>,
             onValChange: (String) -> Unit={})
    {
        val totalBillState = remember {
            mutableStateOf("")
        }
        val validState = remember(totalBillState.value) {
            totalBillState.value.trim().isNotEmpty()
        }
        val keyboardController = LocalSoftwareKeyboardController.current

        val sliderPositionState = remember{
            mutableStateOf(0f)
        }
        val tipPertegance = (sliderPositionState.value*100).toInt()


        TopHeader(totalPerPerson = totalPerPersonState.value)

        Surface(
            modifier = modifier
            .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(13.dp)),
            border = BorderStroke(width = 2.dp, color = Color.LightGray)
        ) {
            Column(modifier=Modifier.padding(13.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start) {
                InputField(
                    valueState = totalBillState,
                    labelId = "Enter Bill",
                    enabled = true,
                    isSingleLine =  true,
                    onAction = KeyboardActions{
                        if (!validState) return@KeyboardActions
                        onValChange(totalBillState.value.trim())
                        keyboardController?.hide()
                    })
             if(validState){

                    Row(modifier=modifier.padding(13.dp),
                        horizontalArrangement = Arrangement.Start) {
                        Text("Split : ",modifier = Modifier.align(
                            alignment = Alignment.CenterVertically))
                        Spacer(modifier.width(120.dp))

                        Row(modifier=Modifier.padding(horizontal = 3.dp),
                            horizontalArrangement = Arrangement.End) {

                            RoundIconButton(
                                modifier = Modifier,
                                imageVector = Icons.Default.Remove,
                                onClick = {
                                    splitByState.value = if (splitByState.value > 1)
                                        splitByState.value - 1
                                    else 1

                                    totalPerPersonState.value =
                                        calculateTotalPerPerson(totalBill=totalBillState.value.toDouble(),
                                            splitBy=splitByState.value,
                                            tipPertegance=tipPertegance
                                        )
                                }

                            )

                            Text("${splitByState.value}",
                                modifier=modifier.align(
                                    Alignment.CenterVertically)
                                .padding(start = 10.dp, end = 10.dp))

                            RoundIconButton(
                                modifier = Modifier,
                                imageVector = Icons.Default.Add,
                               onClick = {
                                   if (splitByState.value<range.last){
                                       splitByState.value = splitByState.value + 1

                                       totalPerPersonState.value =
                                       calculateTotalPerPerson(totalBill=totalBillState.value.toDouble(),
                                          splitBy=splitByState.value,
                                          tipPertegance=tipPertegance
                                       )
                                   }
                                   }
                            )

                        }
                    }
                    //Tip row
                Row(modifier = modifier.padding(horizontal = 13.dp , vertical = 13.dp)) {
                    Text("Tip:" ,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically))

                    Spacer(modifier = Modifier.width(200.dp))

                    Text("$ ${tipAmountState.value}", style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                        .align(alignment = Alignment.CenterVertically))
                }

                Column(verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally)
                {
                    Text("$tipPertegance%", style = MaterialTheme.typography.bodyLarge)

                    Spacer(modifier = Modifier.height(14.dp))

                    Slider(value = sliderPositionState.value,
                        onValueChange = {newVal->
                            sliderPositionState.value = newVal
                            tipAmountState.value=
                                calculateTotalTip(totalBill = totalBillState.value.toDouble(),
                                    tipPertegance = tipPertegance)

                            totalPerPersonState.value =
                                calculateTotalPerPerson(totalBill=totalBillState.value.toDouble(),
                                                splitBy=splitByState.value,
                                            tipPertegance=tipPertegance
                                    )
                        } ,
                        modifier =   Modifier.padding(start = 16.dp, end = 16.dp),
                        steps=100)

                }

                }
                else{
                    Box {  }
                }
            }
        }
    }

