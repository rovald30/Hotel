import { defineStore } from "pinia";
import { ref } from "vue";
import {
  getAllPersons,
  getPersonByIdentityCode,
  addPerson,
} from "../services/modules/PersonAPI";

export const usePersonstore = defineStore("person", () => {
  const responseData = ref(null);
  const peopleInBooking = ref({});;

  /*getAllPersons().then((response) => {
    responseData.value = response;
  });
*/

  function countNumberOfPeopleInBooking(count) {
    for (let i = 1; i <=  count; i++) {
      peopleInBooking.value[i] = {}
    }
  }

  async function getPersonDataFromDB(identityCode, formNumber) {
    getPersonByIdentityCode(identityCode).then((response) => {
      const responseData = response["data"];
      if (responseData !== null) {
        const currentPerson = {
          "idCode": responseData['identityCode'],
          "firstName": responseData['firstName'],
          "lastName": responseData['lastName'],
          "dateOfBirth": responseData['dateOfBirth'],
          "country": responseData['country'],
          "phoneNumber": responseData['phoneNumber'],
          "foundInDatabase": true
        };
        peopleInBooking.value[formNumber] = currentPerson;
        
      } else {
        peopleInBooking.value[formNumber]["foundInDatabase"] = false;
      }
    });
  }

  async function addPersonDataToDB(formNumber) {
    addPerson(
      peopleInBooking.value[formNumber]['idCode'],
      peopleInBooking.value[formNumber]['firstName'],
      peopleInBooking.value[formNumber]['lastName'],
      peopleInBooking.value[formNumber]['dateOfBirth'],
      peopleInBooking.value[formNumber]['country']['name'],
      peopleInBooking.value[formNumber]['phoneNumber']
    ).then((response) => {
      // Todo: We could add some custom notification here?
    });
  }

  return {
    responseData,
    peopleInBooking,
    countNumberOfPeopleInBooking,
    getPersonDataFromDB,
    addPersonDataToDB,
  };
});
