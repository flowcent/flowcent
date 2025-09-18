import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.auth.presentation.component.userOnBoarding.FirstOnBoarding
import com.aiapp.flowcent.auth.presentation.component.userOnBoarding.OnboardingBottomSection
import com.aiapp.flowcent.auth.presentation.component.userOnBoarding.SecondOnBoarding
import com.aiapp.flowcent.auth.presentation.component.userOnBoarding.ThirdOnBoarding
import kotlinx.coroutines.launch

data class OnboardingPage(
    val content: @Composable () -> Unit
)

@Composable
fun UserOnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    state: AuthState
) {
    val pages = listOf(
        OnboardingPage {
            FirstOnBoarding(
                name = state.username,
                onUpdateName = {
                    viewModel.onAction(UserAction.UpdateUsername(it))
                }
            )
        },
        OnboardingPage {
            SecondOnBoarding(
                currentBalance = state.initialBalance,
                savingGoal = state.savingTarget,
                onUpdateCurrentBalance = {
                    viewModel.onAction(UserAction.UpdateInitialBalance(it.toDouble()))
                },
                onUpdateSavingGoal = {
                    viewModel.onAction(UserAction.UpdateSavingTarget(it.toDouble()))
                }
            )
        },
        OnboardingPage {
            ThirdOnBoarding(
                phoneNumber = state.phoneNumber,
                onPhoneNumberChange = {
                    viewModel.onAction(UserAction.UpdatePhoneNumber(it))
                },
                onCountrySelected = {
                    viewModel.onAction(UserAction.UpdateCountry(it))
                }
            )
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





