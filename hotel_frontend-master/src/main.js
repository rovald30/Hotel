import { createApp } from "vue";
import { createPinia } from "pinia";

import App from "./App.vue";
import router from "./router";
import VueEasyLightbox from "vue-easy-lightbox";
import Notifications from "notiwind";
import "vue-select/dist/vue-select.css";
import vSelect from "vue-select";

const app = createApp(App);
const useImage = (fileName, dirName) => {
  return new URL(`/src/assets/img/${dirName}/${fileName}`, import.meta.url);
};

app.config.globalProperties.$image = useImage;

app.use(createPinia());
app.use(router);
app.use(VueEasyLightbox);
app.use(Notifications);

app.provide("$image", useImage);
app.component("v-select", vSelect);

app.mount("#app");
