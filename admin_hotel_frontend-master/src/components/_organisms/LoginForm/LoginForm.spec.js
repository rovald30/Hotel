import { render, screen, fireEvent } from "@testing-library/vue";
import { setActivePinia, createPinia } from "pinia";
import { createTestingPinia } from "@pinia/testing";
import LoginForm from "@/components/_organisms/LoginForm/LoginForm.vue";
import { useAuthStore } from "@/stores/auth.js";

describe("LoginForm.vue", async () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  function getLoginFormMainElement() {
    return screen.getByTestId("login-form-view");
  }

  function getLoadingBarElement() {
    return screen.getByTestId("button-loader");
  }

  function getButtonText() {
    return screen.getByTestId("button-text");
  }

  function getBaseButtonElement() {
    return screen.getByTestId("base-button");
  }

  function getAllBaseInputs() {
    return screen.getAllByTestId("base-input");
  }

  it("renders properly", () => {
    render(LoginForm, {
      global: {
        plugins: [
          createTestingPinia({
            createSpy: vitest.fn,
          }),
        ],
      },
    });

    expect(getLoginFormMainElement()).toBeInTheDocument();
  });

  it('have button with text "Sign in" and by default should be DISABLED', () => {
    render(LoginForm, {
      global: {
        plugins: [
          createTestingPinia({
            createSpy: vitest.fn,
          }),
        ],
      },
    });

    expect(getBaseButtonElement()).toBeInTheDocument();
    expect(getBaseButtonElement()).toBeDisabled();
    expect(getBaseButtonElement()).toHaveTextContent("Sign in");
    expect(getLoadingBarElement()).not.toBeVisible();
  });

  it('button with text "Sign in" SHOULD NOT change to Loading Bar, sine login and password empty', async () => {
    await render(LoginForm, {
      global: {
        plugins: [
          createTestingPinia({
            createSpy: vitest.fn,
          }),
        ],
      },
    });

    await fireEvent.click(getBaseButtonElement());

    expect(getBaseButtonElement()).toBeInTheDocument();
    expect(getBaseButtonElement()).toBeDisabled();
    expect(getBaseButtonElement()).toHaveTextContent("Sign in");

    expect(getLoadingBarElement()).not.toBeVisible();
  });

  it("has inputs with correct data, which passed with store", async () => {
    const login = "test-login";
    const password = "test-password";
    const loginAndPassword = [login, password];

    await render(LoginForm, {
      global: {
        plugins: [
          createTestingPinia({
            initialState: {
              authStore: {
                login,
                password,
              },
            },
          }),
        ],
      },
    });

    const authStore = useAuthStore();

    expect(authStore.login).toBe(login);
    expect(authStore.password).toBe(password);

    for (const [index, inputEl] of getAllBaseInputs().entries()) {
      expect(inputEl).toBeInTheDocument();
      expect(inputEl.value).toBe(loginAndPassword[index]);
    }
  });

  it('button with text "Sign in" should be not disabled, since login and password are provided', async () => {
    const login = "test-login";
    const password = "test-password";

    await render(LoginForm, {
      global: {
        plugins: [
          createTestingPinia({
            initialState: {
              authStore: {
                login,
                password,
              },
            },
          }),
        ],
      },
    });

    expect(getBaseButtonElement()).toHaveTextContent("Sign in");
    expect(getBaseButtonElement()).not.toBeDisabled();
    expect(getLoadingBarElement()).not.toBeVisible();
  });

  it('button with text "Sign in" should be change to Loading Bar, when inputs have values and button is clicked', async () => {
    const loginText = "test-login";
    const passwordText = "test-password";

    await render(LoginForm);

    await fireEvent.update(getAllBaseInputs()[0], loginText);
    await fireEvent.update(getAllBaseInputs()[1], passwordText);

    expect(getButtonText()).not.toBeDisabled();
    expect(getButtonText()).toBeVisible();
    expect(getBaseButtonElement()).toHaveTextContent("Sign in");

    await fireEvent.click(getBaseButtonElement());

    expect(getLoadingBarElement()).toBeVisible();
  });
});
