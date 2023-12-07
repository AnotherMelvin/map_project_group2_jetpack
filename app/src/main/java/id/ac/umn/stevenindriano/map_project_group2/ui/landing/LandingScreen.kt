package id.ac.umn.stevenindriano.map_project_group2.ui.landing

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.ac.umn.stevenindriano.map_project_group2.R
import id.ac.umn.stevenindriano.map_project_group2.auth.SignInState

@Composable
fun LandingScreen(
    state: SignInState,
    onSignInCLick: (mode: Int) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let {error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.xpiry_logo),
            contentDescription = "",
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(
            text = "Xpiry",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 60.sp
        )
        Text(
            text = "Expire Date Tracker",
            style = MaterialTheme.typography.labelLarge,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.size(50.dp))
        Button(
            onClick = { onSignInCLick(1) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_google),
                contentDescription = "",
                modifier = Modifier.padding(8.dp)
            )
            Text(text = "Continue with Google", modifier = Modifier.padding(6.dp))
        }
        Spacer(modifier = Modifier.size(20.dp))
        Button(
            onClick = { onSignInCLick(0) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(text = "Continue as Guest", modifier = Modifier.padding(6.dp))
        }
    }
}