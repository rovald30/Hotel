import "@testing-library/jest-dom";
import { vi } from "vitest";

// NB! IMPORTANT https://vitest.dev/guide/mocking.html#globals
const IntersectionObserverMock = vi.fn(() => ({
  disconnect: vi.fn(),
  observe: vi.fn(),
  takeRecords: vi.fn(),
  unobserve: vi.fn(),
}));

vi.stubGlobal("IntersectionObserver", IntersectionObserverMock);

const matchMedia = vi.fn().mockImplementation((query) => ({
  matches: false,
  media: query,
  onchange: null,
  addListener: vi.fn(), // deprecated
  removeListener: vi.fn(), // deprecated
  addEventListener: vi.fn(),
  removeEventListener: vi.fn(),
  dispatchEvent: vi.fn(),
  as: vi.fn(),
}));

const scrollTo = vi.fn(() => ({}));

const createRange = vi.fn(() => ({
  commonAncestorContainer: global.document.body,
}));

vi.stubGlobal("createRange", createRange);
vi.stubGlobal("scrollTo", scrollTo);
vi.stubGlobal("matchMedia", matchMedia);
vi.stubGlobal("matchMedia", matchMedia);
vi.stubGlobal("as", matchMedia);
