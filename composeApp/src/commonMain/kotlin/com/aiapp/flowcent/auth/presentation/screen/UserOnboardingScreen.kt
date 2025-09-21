import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aiapp.flowcent.auth.presentation.AuthState
import com.aiapp.flowcent.auth.presentation.AuthViewModel
import com.aiapp.flowcent.auth.presentation.UserAction
import com.aiapp.flowcent.auth.presentation.component.userOnBoarding.FirstOnBoarding
import com.aiapp.flowcent.auth.presentation.component.userOnBoarding.OnboardingBottomSection
import com.aiapp.flowcent.auth.presentation.component.userOnBoarding.SecondOnBoarding
import com.aiapp.flowcent.auth.presentation.component.userOnBoarding.ThirdOnBoarding
import flowcent.composeapp.generated.resources.Res
import flowcent.composeapp.generated.resources.ic_arrow_left
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource

data class OnboardingPage(
    val content: @Composable () -> Unit
)

@Composable
fun UserOnboardingScreen(
    modifier: Modifier = Modifier, viewModel: AuthViewModel, state: AuthState
) {
    val pages = listOf(OnboardingPage {
        FirstOnBoarding(
            name = state.username,
            errorMessage = state.inputErrors["username"],
            onUpdateName = {
                viewModel.onAction(UserAction.UpdateUsername(it))
            })
    }, OnboardingPage {
        SecondOnBoarding(
            currentBalance = state.initialBalance,
            savingGoal = state.savingTarget,
            currentBalanceError = state.inputErrors["initialBalance"],
            savingTargetError = state.inputErrors["savingTarget"],
            onUpdateCurrentBalance = {
                viewModel.onAction(UserAction.UpdateInitialBalance(it.toDouble()))
            },
            onUpdateSavingGoal = {
                viewModel.onAction(UserAction.UpdateSavingTarget(it.toDouble()))
            })
    }, OnboardingPage {
        ThirdOnBoarding(
            phoneNumber = state.phoneNumber,
            errorMessage = state.inputErrors["phoneNumber"],
            onPhoneNumberChange = {
                viewModel.onAction(UserAction.UpdatePhoneNumber(it))
            },
            onCountrySelected = {
                viewModel.onAction(UserAction.UpdateCountry(it))
            })
    })

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    fun validatePage(pageIndex: Int): Boolean {
        val errors = mutableMapOf<String, String>()

        when (pageIndex) {
            0 -> { // First page
                if (state.username.isBlank()) {
                    errors["username"] = "Nickname cannot be empty"
                } else if (state.username.length < 3) {
                    errors["username"] = "Nickname must be at least 3 characters"
                }
            }

            1 -> { // Second page
                if (state.initialBalance <= 0.0) {
                    errors["initialBalance"] = "Please enter a valid balance"
                }
                if (state.savingTarget <= 0.0) {
                    errors["savingTarget"] = "Please enter a saving goal"
                }
            }

            2 -> { // Third page
                if (state.phoneNumber.isBlank()) {
                    errors["phoneNumber"] = "Phone number is required"
                }
            }
        }

        viewModel.onAction(UserAction.UpdateInputErrors(errors))
        return errors.isEmpty()
    }

    fun onBackClicked() {
        coroutineScope.launch {
            val currentPage = pagerState.currentPage
            if (currentPage > 0) {
                pagerState.animateScrollToPage(currentPage - 1)
            } else {
                viewModel.onAction(UserAction.NavigateBack)
            }
        }
    }


    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.align(Alignment.Start).padding(24.dp)
        ) {
            Box(
                modifier = Modifier.size(64.dp).clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant).padding(16.dp)
                    .clickable { onBackClicked() }, contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            item {
                HorizontalPager(
                    state = pagerState, modifier = Modifier.weight(1f)
                ) { page ->
                    pages[page].content()
                }
            }

            item {
                OnboardingBottomSection(
                    pagerState = pagerState,
                    onNextClicked = {
                        coroutineScope.launch {
                            val currentPage = pagerState.currentPage
                            val isValid = validatePage(currentPage)
                            if (isValid) {
                                if (currentPage < pages.size - 1) {
                                    pagerState.animateScrollToPage(currentPage + 1)
                                } else {
                                    viewModel.onAction(UserAction.NavigateToChatWelcome)
                                }
                            }
                        }
                    },
                )
            }
        }
    }
}





