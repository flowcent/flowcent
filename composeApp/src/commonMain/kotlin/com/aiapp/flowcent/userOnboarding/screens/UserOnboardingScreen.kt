import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aiapp.flowcent.core.presentation.components.LabeledInputField
import com.aiapp.flowcent.userOnboarding.UserObViewModel
import com.aiapp.flowcent.userOnboarding.UserOnboardingState
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.marcusUpdate
import kotlinx.coroutines.launch
import kottieComposition.KottieCompositionSpec
import kottieComposition.animateKottieCompositionAsState
import kottieComposition.rememberKottieComposition
import org.jetbrains.compose.resources.painterResource
import utils.KottieConstants

data class OnboardingPage(
    val content: @Composable () -> Unit
)

@Composable
fun UserOnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: UserObViewModel,
    state: UserOnboardingState
) {
    val pages = listOf(
        OnboardingPage {
            FirstOnBoarding()
        },
        OnboardingPage {
            SecondOnBoarding()
        }
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF2F2F7))
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            pages[page].content()
        }

        OnboardingBottomSection(
            pagerState = pagerState,
            onNextClicked = {
                coroutineScope.launch {
                    if (pagerState.currentPage < pages.size - 1) {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    } else {
                        // Onboarding finished, handle navigation to the main screen
                    }
                }
            },
        )
    }
}


@Composable
fun OnboardingBottomSection(
    pagerState: PagerState,
    hasSkip: Boolean = false,
    onNextClicked: () -> Unit,
    onSkipClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (hasSkip) Arrangement.SpaceBetween else Arrangement.End
    ) {
        if (hasSkip) {
            Text(
                text = "Skip",
                modifier = Modifier.clickable(onClick = onSkipClicked),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }

        CircularProgressButton(
            progress = (pagerState.currentPage + 1) / pagerState.pageCount.toFloat(),
            onClick = onNextClicked
        )
    }
}

@Composable
fun CircularProgressButton(
    progress: Float,
    onClick: () -> Unit
) {
    val animatedProgress by animateFloatAsState(targetValue = progress, label = "")

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(64.dp)
            .clickable(onClick = onClick)
    ) {
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.size(64.dp),
            strokeWidth = 3.dp,
            color = Color(0xFF6A5AE0),
            trackColor = Color(0xFFE0E0E0)
        )
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(Color(0xFF6A5AE0)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Next",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


@Composable
fun FirstOnBoarding() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(80.dp).background(
                color = MaterialTheme.colorScheme.primary, shape = CircleShape
            ), contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Person Icon",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(40.dp)
            )

        }

        Spacer(modifier = Modifier.height(32.dp))
        LabeledInputField(
            label = "What should we call you?",
            labelSize = 24.sp,
            placeholder = "Enter your nick name",
            value = "",
            onValueChange = { },
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "This is the first onboarding screen",
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                color = Color.Gray
            ),
        )
    }
}

@Composable
fun SecondOnBoarding() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.marcusUpdate),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .aspectRatio(1f),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Elevate",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "This is the second onboarding screen",
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                color = Color.Gray
            ),
        )
    }
}

@Composable
fun AnimatedOnboarding() {
    var botOnboardingAnimation by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        botOnboardingAnimation = Res.readBytes("files/botonboardinganimation.json").decodeToString()
    }

    val botOnboardingComposition = rememberKottieComposition(
        spec = KottieCompositionSpec.File(botOnboardingAnimation) // Or KottieCompositionSpec.Url || KottieCompositionSpec.JsonString
    )

    val botAnimationState by animateKottieCompositionAsState(
        composition = botOnboardingComposition,
        iterations = KottieConstants.IterateForever
    )

    Column(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Image(
//            painter = painterResource(Res.drawable.marcusUpdate),
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxWidth(0.8f)
//                .aspectRatio(1f),
//            contentScale = ContentScale.Fit
//        )
        KottieAnimation(
            composition = botOnboardingComposition,
            progress = { botAnimationState.progress },
            modifier = Modifier.size(300.dp)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Welcome",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "This is the first onboarding screen",
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                color = Color.Gray
            ),
        )
    }
}


