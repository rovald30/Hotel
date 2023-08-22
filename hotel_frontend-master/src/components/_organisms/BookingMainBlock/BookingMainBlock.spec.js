import BookingMainBlock from "@/components/_organisms/BookingMainBlock/BookingMainBlock.vue";
import { render, screen } from "@testing-library/vue";
import { createPinia, setActivePinia } from "pinia";

describe("BookingMainBlock", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  function getBookingMainBlock() {
    return screen.getByTestId("main-block");
  }

  it("renders properly", () => {
    render(BookingMainBlock, {
      global: {
        mocks: {
          $image: (fileName, dirName) => {
            return new URL(
              `/src/assets/img/${dirName}/${fileName}`,
              import.meta.url
            );
          },
        },
      },
    });

    expect(getBookingMainBlock()).toHaveClass("main-block");
  });
});
