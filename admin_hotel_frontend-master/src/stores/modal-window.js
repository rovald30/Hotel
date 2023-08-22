import {defineStore} from "pinia";
import { ref } from "vue";

export const useModalWindowStore = defineStore("modalWindow", () => {

    const showModal = ref(false);

    return {
        showModal,
    }
})