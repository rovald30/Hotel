import { createApp } from "vue";
import { createPinia } from "pinia";
import Notifications from "notiwind";

import App from "./App.vue";
import router from "./router";

import Datepicker from "@vuepic/vue-datepicker";
import "@vuepic/vue-datepicker/dist/main.css";

import "./assets/main.css";

const app = createApp(App);

app.component("Datepicker", Datepicker);
app.use(createPinia());
app.use(router);
app.use(Notifications);

app.mount("#app");
