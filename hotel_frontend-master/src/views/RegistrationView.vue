<script setup>
import ContentWrapper from "@/components/_molecules/ContentWrapper/ContentWrapper.vue";
import Navbar from "@/components/_organisms/Navbar/Navbar.vue";
import { useRegisterStore } from "@/stores/register.js";
import ButtonWithLoader from "@/components/_atoms/ButtonWithLoader/ButtonWithLoader.vue";
import BaseInputCalendar from "@/components/_atoms/BaseInputCalendar/BaseInputCalendar.vue";
import { onBeforeMount, ref, computed, onMounted } from "vue";
import "vue3-tel-input/dist/vue3-tel-input.css";
import countryAPI from "@/services/countryAPI.js";
import { VueTelInput } from "vue3-tel-input";
import { useVuelidate } from "@vuelidate/core";
import { notify } from "notiwind";
import {
  required,
  email,
  minLength,
  sameAs,
  helpers,
} from "@vuelidate/validators";
import BaseInputWithError from "@/components/_atoms/BaseInputWithError/BaseInputWithError.vue";
import TopNotification from "@/components/_atoms/TopNotification/TopNotification.vue";
import BottomNotification from "@/components/_atoms/BottomNotification/BottomNotification.vue";

defineEmits(["focusOut"]);

const registerStore = useRegisterStore();

const registrationFormState = ref("registration");

const validateAge = (birthDayStr) => {
  const today = new Date();
  const dateOfBirth = new Date(birthDayStr);
  let age = today.getFullYear() - dateOfBirth.getFullYear();
  const months = today.getMonth() - dateOfBirth.getMonth();

  if (months < 0 || (months === 0 && today.getDate() < dateOfBirth.getDate())) {
    age--;
  }

  return age > 18;
};

const rules = computed(() => ({
  username: {
    required: helpers.withMessage("Username is required", required),
    min: helpers.withMessage("Username minimum length is 6", minLength(6)),
    // add new validation isAlreadyTaken
  },
  password: {
    required: helpers.withMessage("Password is required", required),
    min: helpers.withMessage("Password minimum length is 6", minLength(6)),
  },
  confirmPassword: {
    required: helpers.withMessage("Please confirm password", required),
    sameAs: helpers.withMessage(
      "Passwords do not match",
      sameAs(registerStore.password)
    ),
  },
  email: {
    email: helpers.withMessage("Invalid email", email),
    required: helpers.withMessage("Email is required", required),
    // add new validation isAlreadyTaken
  },
  firstName: {
    required: helpers.withMessage("Firstname is required", required),
  },
  lastName: {
    required: helpers.withMessage("Lastname is required", required),
  },
  country: {
    nameWithFlag: {
      required: helpers.withMessage("Country is required", required),
    },
  },
  identityCode: {
    required: helpers.withMessage("Identity code is required", required),
  },
  dateOfBirth: {
    required: helpers.withMessage("Date of birth is required", required),
    validateAge: helpers.withMessage(
      "Age must be greater / equal to 18",
      validateAge
    ),
  },
  phoneNumber: {
    required: helpers.withMessage("Phone number is required", required),
  },
  policyAgreement: { sameAs: sameAs(true) },
}));

const v$ = useVuelidate(rules, registerStore);

const phoneNumberInput = {
  placeholder: "Phone number",
};

const countries = ref([
  {
    name: "",
    flagEmoji: "",
    flagSvg: "",
    nameWithFlag: "",
  },
]);

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
  if (phoneObject && phoneObject.valid) {
    registerStore.phoneNumber = phoneObject.number;
  }
};

const signupButton = async () => {
  await v$.value.$validate();
  if (v$.value.$error) {
    return false;
  }

  const backendRegisterAccResponse = await registerStore.registerAccount();
  if (backendRegisterAccResponse.statusCodeValue === 200) {
    registrationFormState.value = "confirm";
  } else {
    await notify(
      {
        group: "bottom",
        title: "Server Error",
        text: backendRegisterAccResponse.body,
      },
      5000
    );
  }
};

onMounted(() => {
  document.addEventListener("keydown", (event) => {
    if (event.code === "Enter") {
      signupButton();
    }
  });
});
</script>

<template>
  <Navbar />

  <Transition name="slide-left">
    <ContentWrapper
      class="flex justify-center items-center p-0 m-0 w-full max-w-2xl"
      v-if="registrationFormState === 'registration'"
    >
      <TopNotification />
      <BottomNotification />
      <div>
        <p class="flex justify-center items-center text-xl pb-4 pt-10">
          Account info
        </p>
        <div class="flex">
          <div>
            <BaseInputWithError
              v-model="registerStore.username"
              @focusout="v$.username.$validate"
              label="Username"
              class="p-2"
              :error-msg="
                v$.username.$error ? v$.username.$errors[0].$message : null
              "
            />

            <BaseInputWithError
              v-model="registerStore.email"
              label="Email"
              @focusout="v$.email.$validate"
              class="p-2"
              :error-msg="v$.email.$error ? v$.email.$errors[0].$message : null"
            />
          </div>
          <div>
            <BaseInputWithError
              v-model="registerStore.password"
              label="Password"
              type="password"
              class="p-2"
              @focusout="v$.password.$validate"
              :error-msg="
                v$.password.$error ? v$.password.$errors[0].$message : null
              "
            />

            <BaseInputWithError
              v-model="registerStore.confirmPassword"
              label="Confirm Password"
              type="password"
              class="p-2"
              @focusout="v$.confirmPassword.$validate"
              :error-msg="
                v$.confirmPassword.$error
                  ? v$.confirmPassword.$errors[0].$message
                  : null
              "
            />
          </div>
        </div>

        <hr class="p-5 border-black" />

        <p class="flex justify-center items-center text-xl pb-4">
          Personal info
        </p>

        <div>
          <div class="flex">
            <BaseInputWithError
              v-model="registerStore.firstName"
              label="Firstname"
              class="p-2"
              @focusout="v$.firstName.$validate"
              :error-msg="
                v$.firstName.$error ? v$.firstName.$errors[0].$message : null
              "
            />
            <BaseInputWithError
              v-model="registerStore.lastName"
              label="Lastname"
              class="p-2"
              @focusout="v$.lastName.$validate"
              :error-msg="
                v$.lastName.$error ? v$.lastName.$errors[0].$message : null
              "
            />
          </div>
          <div class="flex">
            <BaseInputCalendar
              class="w-42 h-9 p-2 mb-1"
              v-model="registerStore.dateOfBirth"
              placeholder="Date of birth"
              autocomplete="on"
              input-type="date"
              label="Date of birth"
              @focusout="v$.dateOfBirth.$validate"
              :error-msg="
                v$.dateOfBirth.$error
                  ? v$.dateOfBirth.$errors[0].$message
                  : null
              "
            />

            <BaseInputWithError
              v-model="registerStore.identityCode"
              label="Person identity code"
              class="p-2 pl-3 -mr-2"
              @focusout="v$.identityCode.$validate"
              :error-msg="
                v$.identityCode.$error
                  ? v$.identityCode.$errors[0].$message
                  : null
              "
            />
          </div>
          <div class="flex">
            <div class="pl-2 mr-4">
              <label class="block text-gray-700 text-sm font-bold mb-2"
                >Country</label
              >
              <v-select
                :options="countries"
                label="nameWithFlag"
                class="w-44 pt-2"
                v-model="registerStore.country"
                @focusout="v$.country.$validate"
              />
              <span
                class="flex absolute text-xs text-red-500"
                v-if="v$.country.$error"
              >
                {{ v$.country.$errors[0].$message }}
              </span>
            </div>
            <div class="pt-2 pl-2 w-52">
              <label class="block text-gray-700 text-sm font-bold mb-2"
                >Phone number</label
              >
              <VueTelInput
                class="h-9"
                :inputOptions="phoneNumberInput"
                @input="onInput"
                @focusout="v$.phoneNumber.$validate"
              />
              <span
                class="flex absolute text-xs text-red-500"
                v-if="v$.phoneNumber.$error"
              >
                {{ v$.phoneNumber.$errors[0].$message }}
              </span>
            </div>
          </div>
        </div>

        <div
          class="flex justify-center items-center pb-5 pt-10"
          :class="{ 'text-red-500': v$.policyAgreement.$error }"
        >
          <input
            type="checkbox"
            class="w-5 h-5 ml-4 mr-5 border border-black appearance-none checked:bg-blue-600"
            :class="{ 'border border-red-500': v$.policyAgreement.$error }"
            @click="
              registerStore.policyAgreement === false
                ? (registerStore.policyAgreement = true)
                : (registerStore.policyAgreement = false)
            "
          />
          <p class="w-96 text-sm">
            By creating an account, you agree with our Terms & conditions and
            Privacy statement.
          </p>
        </div>

        <ButtonWithLoader
          class="h-12"
          buttonText="Sign up"
          @click="signupButton"
        />
      </div>
    </ContentWrapper>

    <ContentWrapper
      v-else
      class="flex h-screen w-screen justify-center place-items-center"
    >
      <div
        class="border border-black shadow-green-600/10 shadow-md p-10 bg-white"
      >
        <p class="pb-14">&#127881; Congratulations!</p>
        <p class="pb-4">
          Your account <b>{{ registerStore.username }}</b> is successfully
          created! &#9989;
        </p>
        <p>
          Please check your email <b>{{ registerStore.email }}</b> for
          confirmation link!
        </p>
        <p>Link will be expires in <b>15 minutes</b>. &#128338;</p>
      </div>
    </ContentWrapper>
  </Transition>
</template>

<style scoped>
.style-chooser .vs__dropdown-menu {
  height: 50px;
}
.slide-left-enter-active,
.slide-left-leave-active {
  transition: all 1s ease-out;
}

.slide-left-enter-from {
  opacity: 0;
}

.slide-left-leave-to {
  opacity: 0;
  transform: translateX(-500px);
}
</style>
