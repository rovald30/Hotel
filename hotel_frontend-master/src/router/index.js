import { createRouter, createWebHistory } from "vue-router";
import MainHotelView from "@/views/MainHotelView.vue";
import RoomsView from "@/views/RoomsView.vue";
import SpecialOffersView from "@/views/SpecialOffersView.vue";
import LoyaltyView from "@/views/LoyaltyView.vue";
import AboutUsView from "@/views/AboutUsView.vue";
import BookingView from "@/views/BookingView.vue";
import RegistrationView from "@/views/RegistrationView.vue";
import EmailConfirmation from "@/views/EmailConfirmation.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "hotel",
      component: MainHotelView,
    },
    {
      path: "/rooms",
      name: "rooms",
      component: RoomsView,
    },
    {
      path: "/special-offers",
      name: "special-offers",
      component: SpecialOffersView,
    },
    {
      path: "/loyalty",
      name: "loyalty",
      component: LoyaltyView,
    },
    {
      path: "/about-us",
      name: "about-us",
      component: AboutUsView,
    },
    {
      path: "/signup",
      name: "signup",
      component: RegistrationView,
    },
    {
      path: "/booking",
      name: "booking",
      component: BookingView,
    },
    {
      path: "/confirm",
      name: "confirm",
      component: EmailConfirmation,
    },
  ],
});

export default router;
