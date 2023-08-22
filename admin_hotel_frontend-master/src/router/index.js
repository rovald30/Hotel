import { createRouter, createWebHistory } from "vue-router";
import AdminPanelMainView from "@/views/main-views/AdminPanelMainView.vue";
import RoomsMainView from "@/views/main-views/RoomsMainView.vue";
import BookingsMainView from "@/views/main-views/BookingsMainView.vue";
import UsersMainView from "@/views/main-views/UsersMainView.vue";
import SpecialOffersMainView from "@/views/main-views/SpecialOffersMainView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: AdminPanelMainView,
    },
    {
      path: "/bookings",
      name: "bookings",
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: BookingsMainView,
    },
    {
      path: "/rooms",
      name: "rooms",
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: RoomsMainView,
    },
    {
      path: "/users",
      name: "users",
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: UsersMainView,
    },
    {
      path: "/special-offers",
      name: "special-offers",
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: SpecialOffersMainView,
    },
  ],
});

export default router;
