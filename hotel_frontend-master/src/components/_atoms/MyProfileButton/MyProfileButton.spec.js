import MyProfileButton from "@/components/_atoms/MyProfileButton/MyProfileButton.vue";
import { render, screen } from "@testing-library/vue";

describe("MyProfileButton.vue", () => {
  function getMyProfileButton() {
    return screen.getByTestId("myprofile-button");
  }

  function getIconUserCircle() {
    return screen.getByTestId("user-icon-circle");
  }

  it("renders properly", () => {
    render(MyProfileButton);

    expect(getMyProfileButton());
    expect(getMyProfileButton()).toBeInTheDocument();
  });

  it("has class login", () => {
    render(MyProfileButton);

    expect(getMyProfileButton()).toHaveClass("login");
  });

  it("has icon - IconUserCircle", () => {
    render(MyProfileButton);

    expect(getIconUserCircle());
    expect(getIconUserCircle()).toBeInTheDocument();
    expect(getIconUserCircle()).toHaveClass("icon");

    expect(getMyProfileButton()).toContainElement(getIconUserCircle());
  });

  it("has test - MY PROFILE", () => {
    render(MyProfileButton);

    expect(getMyProfileButton()).toHaveTextContent("MY PROFILE");
  });
});
