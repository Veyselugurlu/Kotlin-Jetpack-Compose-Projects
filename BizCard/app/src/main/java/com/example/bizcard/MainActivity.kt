package com.example.bizcard

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bizcard.ui.theme.BizCardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BizCardTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GreetBizCard(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun GreetBizCard(modifier: Modifier = Modifier) {
    val buttonClickedState = remember {
        mutableStateOf(false)
    }
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Card(
            modifier = Modifier
                .width(200.dp)
                .height(300.dp)
                .padding(12.dp),
            shape = RoundedCornerShape(corner = CornerSize(15.dp)),
            colors = CardDefaults.cardColors(
                containerColor = Color.LightGray), // Arka plan rengi
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp),)
        {
            Column(modifier
                .wrapContentSize()
                .fillMaxWidth(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                )
            {

                CreateImageProfile()
                Divider(thickness = 3.dp)
                CreateInfo()
                Button(
                    onClick = {
                       buttonClickedState.value = !buttonClickedState.value
                    }
                ) {
                    Text("Portfofilo", style = MaterialTheme.typography.bodyMedium)
                }

                if(buttonClickedState.value){
                    Content()
                }
                else{
                    Box(){

                    }
                }

            }
        }
    }
}


@Composable
fun Content(){
    Box(Modifier
        .fillMaxHeight()
        .fillMaxWidth().padding(5.dp)){
            Surface(Modifier.fillMaxHeight().fillMaxWidth().padding(5.dp),
                shape = RoundedCornerShape(corner = CornerSize(5.dp)),
                border = BorderStroke(width = 7.dp, color = Color.Red)) {
                Portofilio(data= listOf("Portofilio1","portofilio2","portofilio3"))
            }
    }
}

@Composable
fun Portofilio(data: List<String>) {
    LazyColumn {
        items(data){ item->
            Card(modifier = Modifier.padding(2.dp)
                .fillMaxWidth()
               .padding(15.dp),
                    shape = RectangleShape) {
                Row(modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(6.dp)) {
                        CreateImageProfile(modifier = Modifier.size(100.dp))
                    Column(modifier = Modifier.padding(6.dp).align(Alignment.CenterVertically)) {
                        Text(text = item, fontWeight = FontWeight.Bold)
                        Text("A Great Project", style = MaterialTheme.typography.bodySmall)
                    }

                }
                }
            }
    }
}

@Composable
private fun CreateInfo() {
    Column(modifier = Modifier.padding(15.dp)) {
        Text(
            "Veysel Ugurlu",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            "Android Compose Programmer",
            modifier = Modifier.padding(5.dp)
        )
        Text(
            "vv@composer",
            modifier = Modifier.padding(5.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun CreateImageProfile(modifier: Modifier=Modifier) {
    Surface(
        modifier
            .size(150.dp)
            .padding(5.dp),
        shape = CircleShape,
        border = BorderStroke(0.5.dp, color = Color.LightGray),
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    )
    {
        Image(
            painter = painterResource(id = R.drawable.profil),
            contentDescription = "profile image",
            modifier = modifier.size(135.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BizCardTheme {
        GreetBizCard()
    }
}