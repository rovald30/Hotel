import { screen, render } from "@testing-library/vue";
import BaseInputCalendar from "@/components/_atoms/BaseInputCalendar/BaseInputCalendar.vue";

describe("BaseInputCalendar.vue", () => {
  function getBaseInputCalendarElement() {
    return screen.getByTestId("base-input-calendar");
  }

  it("renders properly", () => {
    render(BaseInputCalendar);
    expect(getBaseInputCalendarElement()).toHaveClass("identity-input mb-4");
  });

  it("renders label, if label prop is passed", () => {
    const labelText = "TEST";
    render(BaseInputCalendar, {
      props: {
        label: labelText,
      },
    });

    expect(getBaseInputCalendarElement()).toHaveTextContent(labelText);
  });

  it("renders properly value what passed", () => {
    const modalValue = "TEST";
    render(BaseInputCalendar, {
      props: {
        modalValue,
      },
    });

    expect(getBaseInputCalendarElement()).toHaveAttribute(
      "modalValue",
      modalValue
    );
  });

  it("appears attributes to input component, what was passed", () => {
    render(BaseInputCalendar, {
      attrs: {
        test: "test",
        placeholder: "Password",
        type: "password",
      },
    });

    expect(getBaseInputCalendarElement()).toHaveAttribute("test", "test");
    expect(getBaseInputCalendarElement()).toHaveAttribute(
      "placeholder",
      "Password"
    );
    expect(getBaseInputCalendarElement()).toHaveAttribute("type", "password");
  });
});
