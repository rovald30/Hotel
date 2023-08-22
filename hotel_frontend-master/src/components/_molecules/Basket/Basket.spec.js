import Basket from "@/components/_molecules/Basket/Basket.vue";
import { screen, render, fireEvent } from "@testing-library/vue";
import { setActivePinia, createPinia } from "pinia";

describe("Basket", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  function getBasket() {
    return screen.getByTestId("basket");
  }

  function getBasketStatus() {
    return screen.getByTestId("basket-status");
  }

  function getPlusButton() {
    return screen.getByTestId("plus-button");
  }

  function getMinusButton() {
    return screen.getByTestId("minus-button");
  }

  it("has relevant style classes", () => {
    render(Basket);
    expect(getBasket()).toHaveClass("mt-5 border-t-2 border-gray-400");
  });

  it("is initialized with 0 elements in basket", async () => {
    await render(Basket, {
      props: {
        productName: "testElement",
      },
    });

    expect(getBasketStatus()).toHaveTextContent("Currently in basket: 0");
  });

  it("increases number of elements in basket when increment button clicked", async () => {
    render(Basket, {
      props: {
        productName: "testElement",
      },
    });

    const button = getPlusButton();
    await fireEvent.click(button);

    expect(getBasketStatus()).toHaveTextContent("Currently in basket: 1");
  });

  it("clicking plus sign button increases number of element in basket", async () => {
    await render(Basket, {
      props: {
        productName: "testElement",
      },
    });

    const button = getPlusButton();
    await fireEvent.click(button);

    expect(getBasketStatus()).toHaveTextContent("Currently in basket: 1");
  });

  it("clicking minus sign button decreases number of element in basket", async () => {
    await render(Basket, {
      props: {
        productName: "testElement",
      },
    });

    const plusButton = getPlusButton();
    const minusButton = getMinusButton();
    await fireEvent.click(plusButton);
    await fireEvent.click(plusButton);

    expect(getBasketStatus()).toHaveTextContent("Currently in basket: 2");

    await fireEvent.click(minusButton);

    expect(getBasketStatus()).toHaveTextContent("Currently in basket: 1");
  });

  it("clicking minus sign button does not decrease number of elements in basket below zero", async () => {
    await render(Basket, {
      props: {
        productName: "testElement",
      },
    });

    const minusButton = getMinusButton();

    await fireEvent.click(minusButton);

    expect(getBasketStatus()).toHaveTextContent("Currently in basket: 0");
  });
});
