import { screen, render } from "@testing-library/vue";
import BaseButton from "@/components/_atoms/BaseButton/BaseButton.vue";

describe("BaseButton.vue", () => {
  function getBaseButtonElement() {
    return screen.getByTestId("base-button");
  }

  it("renders properly", () => {
    render(BaseButton);
    expect(getBaseButtonElement()).toHaveClass(
      "hover:bg-blue-500 text-gray-700 hover:text-white px-5 border-2 border-gray-800 hover:border-transparent rounded mx-0.5"
    );
  });

  it("renders textContent, if textContent prop is passed", () => {
    const buttonText = "I am button";
    render(BaseButton, {
      props: {
        textContent: buttonText,
      },
    });

    expect(getBaseButtonElement()).toHaveTextContent(buttonText);
  });
});
