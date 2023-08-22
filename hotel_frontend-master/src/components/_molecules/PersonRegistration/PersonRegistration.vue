<script setup>
import { onBeforeMount, ref, computed, onMounted } from "vue";
import BaseInput from "@/components/_atoms/BaseInput/BaseInput.vue";
import BaseButton from "@/components/_atoms/BaseButton/BaseButton.vue";
import { usePersonstore } from "@/stores/person";
import countryAPI from "@/services/countryAPI.js";
import { VueTelInput } from "vue3-tel-input";
import { useRoomsStore } from "@/stores/rooms.js";

const { peopleInBooking, getPersonDataFromDB, addPersonDataToDB } =
  usePersonstore();

const props = defineProps({
  formNumber: Number
});

let wasIdCodeChecked = false;
const personStore = usePersonstore();

const countries = ref([
  {
    name: "",
    flagEmoji: "",
    flagSvg: "",
    nameWithFlag: "",
  },
]);

const phoneNumberInput = {
  placeholder: "Phone number",
};

onBeforeMount(async () => {
  await countryAPI.getAllCountries().then((response) => {
    response.map((el) =>
      countries.value.push({
        nameWithFlag: el.flag + " " + el.name.common,
        name: el.name.common,
        flagEmoji: el.flag,
        flagSvg: el.flags.svg,
      })
    );
  });
});

const onInput = (phoneText, phoneObject, input) => {
  if (phoneObject) {
    personStore.peopleInBooking[props.formNumber].phoneNumber = phoneObject.number;
  }
};

async function checkDbOnFocusOut(idCode) {
  if (!wasIdCodeChecked) {
    wasIdCodeChecked = true;
    getPersonDataFromDB(idCode, props.formNumber);
  } else {
    wasIdCodeChecked = false;
  }
}

function getMyName(name='John') {
  return name
}
</script>

<template>
  <div class="p-10 border-4 border-gray-200 rounded-lg m-2">
    <BaseInput
      v-model="personStore.peopleInBooking[props.formNumber]['idCode']"
      label="Identiy Code"
      placeholder="61107121760"
      autocomplete="on"
      @focusout="checkDbOnFocusOut(personStore.peopleInBooking[props.formNumber]['idCode'])"
    />

    <BaseInput
      v-model="personStore.peopleInBooking[props.formNumber]['firstName']"
      id="idFirstName"
      label="First Name"
      placeholder="John"
      autocomplete="on"
      :disabled="personStore.peopleInBooking[props.formNumber]['foundInDatabase']"
    />

    <BaseInput
      v-model="personStore.peopleInBooking[props.formNumber]['lastName']"
      label="Last Name"
      placeholder="Smith"
      autocomplete="on"
      :disabled="personStore.peopleInBooking[props.formNumber]['foundInDatabase']"
    />

    <BaseInput
      v-model="personStore.peopleInBooking[props.formNumber]['dateOfBirth']"
      label="Date of Birth"
      type="date"
      autocomplete="on"
      :disabled="personStore.peopleInBooking[props.formNumber]['foundInDatabase']"
    />

    <div class="pl-2 mr-4">
      <label class="block text-gray-700 text-sm font-bold mb-2">Country</label>
      <v-select
        :options="countries"
        label="nameWithFlag"
        class="w-44 pt-2"
        v-model="personStore.peopleInBooking[props.formNumber]['country']"
        :disabled="personStore.peopleInBooking[props.formNumber]['foundInDatabase']"
      />
    </div>

    <div class="pt-2 pl-2 w-52">
      <label class="block text-gray-700 text-sm font-bold mb-2"
        >Phone number</label
      >
      <VueTelInput
        class="h-9"
        :inputOptions="phoneNumberInput"
        @input="onInput"
        :disabled="personStore.peopleInBooking[props.formNumber]['foundInDatabase']"
      />
    </div>
  </div>
</template>
