import { defineStore } from "pinia";
import { ref } from "vue";
import PersonsAPI from "../services/modules/PersonAPI";

export const usePersonStore = defineStore("person", () => {
  const responseData = ref(null);
  const peopleInBooking = ref({});

  const getAllPersonsWithParams = async (sortRuleObj) => {
    await PersonsAPI.getAllPersons(sortRuleObj).then((response) => {
      responseData.value = response.data;
    });
  };

  return { responseData, peopleInBooking, getAllPersonsWithParams };
});
