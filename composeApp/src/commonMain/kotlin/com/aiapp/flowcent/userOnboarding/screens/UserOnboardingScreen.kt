import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.aiapp.flowcent.userOnboarding.UserAction
import com.aiapp.flowcent.userOnboarding.UserObViewModel
import com.aiapp.flowcent.userOnboarding.UserOnboardingState
import com.aiapp.flowcent.userOnboarding.components.FirstOnBoarding
import com.aiapp.flowcent.userOnboarding.components.OnboardingBottomSection
import com.aiapp.flowcent.userOnboarding.components.SecondOnBoarding
import com.aiapp.flowcent.userOnboarding.components.ThirdOnBoarding
import kotlinx.coroutines.launch

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
        },
        OnboardingPage {
            ThirdOnBoarding()
        }
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
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
                        viewModel.onAction(UserAction.NavigateToChatOnboard)
                    }
                }
            },
        )
    }
}





