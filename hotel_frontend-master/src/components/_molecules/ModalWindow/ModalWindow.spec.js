import { render, screen } from "@testing-library/vue";
import ModalWindow from "@/components/_molecules/ModalWindow/ModalWindow.vue";
import { setActivePinia, createPinia } from "pinia";
import { createTestingPinia } from "@pinia/testing";
import { useModalWindowStore } from "@/stores/modalWindow";

describe("ModalWindow.vue", () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });

  function getModalWindow() {
    return screen.getByTestId("modal-window");
  }

  function getDialogPanel() {
    return screen.getByTestId("dialog-panel");
  }

  const global = {
    plugins: [
      createTestingPinia({
        initialState: {
          modalWindow: {
            isOpen: true,
          },
        },
      }),
    ],
  };

  it('is present, when ref "isOpen" is true', async () => {
    await render(ModalWindow, {
      global,
    });

    const modalWindowStore = useModalWindowStore();

    expect(modalWindowStore.isOpen).toBeTruthy();
    expect(getModalWindow()).toBeInTheDocument();
  });

  it("has the text passed from slot", async () => {
    const testSlotText = "TEST SLOT";

    await render(ModalWindow, {
      global,
      slots: {
        default: `<p>${testSlotText}</p>`,
      },
    });

    expect(getModalWindow()).toBeInTheDocument();

    expect(getDialogPanel()).toBeInTheDocument();
    expect(getDialogPanel()).toContainHTML(`<p>${testSlotText}</p>`);
    expect(getDialogPanel()).toHaveTextContent(testSlotText);
  });
});
