    package com.example.lab_week_09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.tooling.CompositionData
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.foundation.layout.fillMaxWidth
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme
import com.example.lab_week_09.ui.theme.OnBackgroundItemText
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton
import com.squareup.moshi.Json
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.Types

    class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    App(
                        navController = navController
                    )
                }
            }
        }
    }
}

    @Composable
    fun App(navController: NavHostController){
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home"){
                Home { navController.navigate("resultContent/?listData=$it")
                }
            }
            composable(
                "resultContent/?listData={listData}",
                arguments = listOf(navArgument("listData"){
                    type = NavType.StringType
                })
            ) {
                ResultContent(
                    it.arguments?.getString("listData").orEmpty()
                )
            }
        }
    }

    data class Student(
        var name: String
    )
    @Composable
    fun Home(
        navigateFromHomeToResult: (String) -> Unit
    ) {
        val listData = remember {
            mutableStateListOf(
                Student("Tanu"),
                Student("Tina"),
                Student("Tono")
            )
        }
        var inputField = remember { mutableStateOf(Student("")) }
        var error = remember {mutableStateOf<String?>(null)}
        var errorEmptyMessage = stringResource(id = R.string.error_empty)

        HomeContent(
            listData,
            inputField.value,
            {input -> inputField.value = inputField.value.copy(input)
            if (error.value != null) {
                error.value = null
            }},
            {
                if (inputField.value.name.isNotBlank()){
                    listData.add(inputField.value)
                    inputField.value = Student("")
                    error.value = null
                } else {
                    error.value = errorEmptyMessage
                }
            },
            {
                val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                    .build()

                val listType = Types.newParameterizedType(
                    List::class.java,
                    Student::class.java
                )
                val adapter: JsonAdapter<List<Student>> = moshi.adapter(listType)
                val jsonString = adapter.toJson(listData.toList())
                navigateFromHomeToResult(jsonString)
            },
            error = error.value


        )
        }

    @Composable
    fun HomeContent(
        listData: SnapshotStateList<Student>,
        inputField: Student,
        onInputValueChange: (String) -> Unit,
        onButtonClick: () -> Unit,
        navigateFromHomeToResult: () -> Unit,
        error: String?
    ) {
        LazyColumn {
            item {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OnBackgroundTitleText(text = stringResource(id = R.string.enter_item))

                    TextField(
                        value = inputField.name,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text
                        ),
                        onValueChange = {
                            onInputValueChange(it)
                        },
                        isError = error != null,
                        supportingText = {
                            if (error != null){
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    )
                    Row{
                       PrimaryTextButton(text = stringResource(id = R.string.button_click)) {
                           onButtonClick()
                       }
                        PrimaryTextButton(text = stringResource(id = R.string.button_navigate)) {
                            navigateFromHomeToResult()
                        }
                    }

                }
            }
            items(listData) {item ->
                Column(
                    modifier = Modifier.padding(vertical = 4.dp).fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OnBackgroundItemText(text = item.name)
                }
            }

        }
    }

@Composable
fun ResultContent(listData: String){
    val studentList: List<Student> = remember(listData) {
        try {
            val moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            val listType = Types.newParameterizedType(List::class.java, Student::class.java)
            val adapter: JsonAdapter<List<Student>> = moshi.adapter(listType)

            adapter.fromJson(listData) ?: emptyList()
        } catch (e: Exception){
            e.printStackTrace()
            emptyList()
        }
    }
    LazyColumn(
        modifier = Modifier
        .padding(vertical = 4.dp)
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(studentList.isEmpty()){
            item {
                OnBackgroundItemText(text = "No data received")
            }
        } else {
            items(studentList) { student ->
                OnBackgroundItemText(text = student.name)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHome(){
    Home(navigateFromHomeToResult = {})
}