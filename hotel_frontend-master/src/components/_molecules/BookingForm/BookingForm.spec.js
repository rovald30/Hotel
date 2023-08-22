import BookingForm from "@/components/_molecules/BookingForm/BookingForm.vue";
import { screen, render } from "@testing-library/vue";
import { setActivePinia, createPinia } from "pinia";
import { createTestingPinia } from "@pinia/testing";

describe("BookingForm", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  function getBookingForm() {
    return screen.getByTestId("booking-form");
  }

  it("has labels with `Check in` and `Check out`", () => {
    render(BookingForm);

    expect(getBookingForm()).toHaveTextContent("Check in");
    expect(getBookingForm()).toHaveTextContent("Check out");
  });

  it("has labels `Adults` and `Children` amounts", () => {
    render(BookingForm);

    expect(getBookingForm()).toHaveTextContent("Adults");
    expect(getBookingForm()).toHaveTextContent("Children");
  });

  it("has default amount value 1 for `Adults`", () => {
    render(BookingForm);

    expect(
      screen.getByText("Adults").nextElementSibling.children[1]
    ).toHaveTextContent("1");
  });

  it("has default amount value 0 for `Children`", () => {
    render(BookingForm);

    expect(
      screen.getByText("Children").nextElementSibling.children[1]
    ).toHaveTextContent("0");
  });

  it("has inline style by default", () => {
    render(BookingForm);

    expect(getBookingForm()).toHaveClass("inline");
  });

  it("has flex-col style if state of bookingFormMob changed to true in Pinia store", async () => {
    await render(BookingForm, {
      global: {
        plugins: [
          createTestingPinia({
            initialState: {
              bookingForm: {
                bookingFormMob: true,
              },
            },
          }),
        ],
      },
    });

    expect(getBookingForm()).toHaveClass("flex-col");
  });
});
