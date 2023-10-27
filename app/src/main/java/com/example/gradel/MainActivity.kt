@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.gradel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gradel.Data.DataForm
import com.example.gradel.Data.DataSource.Status
import com.example.gradel.Data.DataSource.jenis
import com.example.gradel.ui.theme.GradelTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GradelTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   TampilLayout()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
@Composable
fun TampilLayout(
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(20.dp)
        ) {
            TampilForm()
        }
    }
}
@Composable
fun TampilForm(cobaViewModel: CobaViewModel = viewModel()){
    var textNama by remember { mutableStateOf("") }
    var textTlp by remember { mutableStateOf("") }
    var textalm by remember { mutableStateOf("") }
    val context = LocalContext.current
    val dataForm: DataForm
    val uiState by cobaViewModel.uiState.collectAsState()
    dataForm = uiState

    OutlinedTextField(
        value = textNama,
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Nama Lengkap")},
        onValueChange = {textNama = it}
    )
    OutlinedTextField(
        value = textTlp,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Telpon")},
        onValueChange = {textTlp = it}
    )
    OutlinedTextField(
        value = textalm,
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = "Alamat")},
        onValueChange = {textalm = it}
    )
    SelectJK(options = jenis.map { id -> context.resources.getString(id)},
        onSelectionChanged = {cobaViewModel.setJenisK(it)})
    status(options = Status.map { id -> context.resources.getString(id)},
        onSelectionChanged = {cobaViewModel.setStatus(it)})
    Button(modifier = Modifier.fillMaxWidth(),
        onClick = { cobaViewModel.insertData(textNama, textTlp, textalm, dataForm.sex, dataForm.sts) }
    ) {
        Text(text = stringResource(R.string.submit),
            fontSize = 16.sp)
    }
    Spacer(modifier = Modifier.height(100.dp))
    TextHasil(namanya = cobaViewModel.namaUsr, telponnya = cobaViewModel.noTlp, alamatnya = cobaViewModel.alamat, jenisnya = cobaViewModel.jenisKl)

}
@Composable
fun status(options: List<String>,
           onSelectionChanged: (String) -> Unit = {}
){
    var selectedValue by rememberSaveable {
        mutableStateOf("")}

    Column (modifier = Modifier.padding(16.dp)
    ){
        Text(
            text = "Status:",
            color = Color.Black,
            textAlign = TextAlign.Left
        )

        options.forEach{ item ->
            Row(
                modifier = Modifier.selectable(
                    selected = selectedValue == item,
                    onClick = {
                        selectedValue = item
                        onSelectionChanged(item)
                    }
                ),
                verticalAlignment = Alignment.CenterVertically
            ){
                RadioButton(selected = selectedValue == item,
                    onClick = {selectedValue = item
                        onSelectionChanged(item)
                    }
                )
                Text(text = item)
            }
        }
    }
}
@Composable
fun SelectJK(
    options: List<String>,
    onSelectionChanged: (String) -> Unit = {}
){
    var selectedValue by rememberSaveable {
        mutableStateOf("")}

    Column (modifier = Modifier.padding(20.dp)){
        Text(
            text = "Jenis Kelamin:",
            color = Color.Black,
            textAlign = TextAlign.Left
        )
        options.forEach{ item ->
            Row(
                modifier = Modifier.selectable(
                    selected = selectedValue == item,
                    onClick = {
                        selectedValue = item
                        onSelectionChanged(item)
                    }
                ),
                verticalAlignment = Alignment.CenterVertically
            ){
                RadioButton(selected = selectedValue == item,
                    onClick = {selectedValue = item
                        onSelectionChanged(item)
                    }
                )
                Text(text = item
                )
            }
        }
    }
}

@Composable
fun TextHasil(namanya: String, telponnya: String, alamatnya: String, jenisnya: String) {
    ElevatedCard( modifier = Modifier
        .height(200.dp)
        .width(350.dp), elevation = CardDefaults.cardElevation( defaultElevation = 6.dp)) {
        Text(text = "Nama: " + namanya,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 4.dp)
        )
        Text(text = "Telepon: " + telponnya,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
        Text(text = "Alamat: " + alamatnya,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )
        Text(text = "Jenis kelamin: " + jenisnya,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GradelTheme {
        TampilLayout()
    }
}
