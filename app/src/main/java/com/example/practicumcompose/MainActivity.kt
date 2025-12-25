package com.example.practicumcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PreviewContactWithPhoto()
        }
    }
}

@Composable
fun ContactDetails(contact: Contact) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .padding(top = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Фото или инициалы
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(120.dp)
        ) {
            if (contact.imageRes != null) {
                Image(
                    painter = painterResource(id = contact.imageRes),
                    contentDescription = "Фото",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.Gray)
                ) {
                    val initials = "${contact.name.first()}${contact.familyName.first()}"
                    Text(
                        text = initials,
                        fontSize = 30.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))


        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = contact.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = " ${contact.familyName}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )


            if (contact.isFavorite) {
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = android.R.drawable.star_big_on),
                    contentDescription = "Избранный",
                )
            }
        }


        contact.surname?.let { surname ->
            Text(
                text = surname,
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.Start
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Телефон: ",
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.width(80.dp)
                )
                Text(
                    text = contact.phone,
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Normal
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "Адрес: ",
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .width(80.dp)
                        .padding(top = 2.dp)
                )
                Text(
                    text = contact.address,
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Normal

                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            contact.email?.let { email ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "E-mail: ",
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.width(80.dp)
                    )
                    Text(
                        text = email,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Normal
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewContactWithoutPhoto() {
    ContactDetails(
        contact = Contact(
            name = "Иван",
            surname = "Петрович",
            familyName = "Сидоров",
            imageRes = null,
            isFavorite = true,
            phone = "+7 495 123-45-67",
            address = "г. Москва, ул. Примерная, д. 1",
            email = "ivan@example.com"
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewContactWithPhoto() {

    ContactDetails(
        contact = Contact(
            name = "Мария",
            surname = null,
            familyName = "Иванова",
            imageRes = R.drawable.cat, // Временно null для теста
            isFavorite = false,
            phone = "+7 495 987-65-43",
            address = "г. Санкт-Петербург, Невский пр., д. 100",
            email = null
        )
    )
}

