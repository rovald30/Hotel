import { screen, render } from "@testing-library/vue";
import BaseInput from "@/components/_atoms/BaseInput/BaseInput.vue";

describe("BaseInput.vue", () => {
  function getBaseInputElement() {
    return screen.getByTestId("base-input");
  }

  it("renders properly", () => {
    render(BaseInput);
    expect(getBaseInputElement()).toHaveClass("identity-input mb-4");
  });

  it("renders label, if label prop is passed", () => {
    const labelText = "TEST";
    render(BaseInput, {
      props: {
        label: labelText,
      },
    });

    expect(getBaseInputElement()).toHaveTextContent(labelText);
  });

  it("renders properly value what passed", () => {
    const modalValue = "TEST";
    render(BaseInput, {
      props: {
        modalValue,
      },
    });

    expect(getBaseInputElement()).toHaveAttribute("modalValue", modalValue);
  });

  it("appears attributes to input component, what was passed", () => {
    render(BaseInput, {
      attrs: {
        test: "test",
        placeholder: "Password",
        type: "password",
      },
    });

    expect(getBaseInputElement()).toHaveAttribute("test", "test");
    expect(getBaseInputElement()).toHaveAttribute("placeholder", "Password");
    expect(getBaseInputElement()).toHaveAttribute("type", "password");
  });
});
