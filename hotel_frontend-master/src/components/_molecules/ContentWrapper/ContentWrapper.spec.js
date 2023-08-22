import ContentWrapper from "@/components/_molecules/ContentWrapper/ContentWrapper.vue";
import { render, screen } from "@testing-library/vue";

describe("ContentWrapper.vue", () => {
  function getContentWrapper() {
    return screen.getByTestId("content-wrapper");
  }

  it("renders properly", () => {
    render(ContentWrapper);

    expect(getContentWrapper());
    expect(getContentWrapper()).toBeInTheDocument();
  });

  it("has content what passed via slot", () => {
    const slotText = "TEST";

    render(ContentWrapper, {
      slots: {
        default: `<p data-testid="slot">${slotText}</p>`,
      },
    });

    const slotElement = screen.getByTestId("slot");

    expect(getContentWrapper());
    expect(getContentWrapper()).toHaveTextContent(slotText);
    expect(getContentWrapper()).toContainElement(slotElement);
  });
});
