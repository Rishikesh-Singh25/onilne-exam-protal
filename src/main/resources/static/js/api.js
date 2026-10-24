/**
 * Shared frontend helper for every page. Centralizing fetch here
 * means: (1) the JWT is attached consistently on every call, and
 * (2) a 401 from ANY page automatically bounces back to login,
 * instead of each page handling auth failure differently.
 */
const API_BASE = "http://localhost:8080/api";

function getToken() { return localStorage.getItem("token"); }
function getRole() { return localStorage.getItem("role"); }
function getName() { return localStorage.getItem("name"); }
function getUserId() { return localStorage.getItem("userId"); }

function setAuth(data) {
  localStorage.setItem("token", data.token);
  localStorage.setItem("role", data.role);
  localStorage.setItem("name", data.name);
  localStorage.setItem("userId", data.userId);
}

function clearAuth() {
  localStorage.clear();
}

function logout() {
  clearAuth();
  window.location.href = "/pages/login.html";
}

/** Call at the top of any protected page. Redirects to login if
 *  there's no token, or to the correct dashboard if the role
 *  doesn't match what this page expects. */
function requireRole(expectedRole) {
  const token = getToken();
  const role = getRole();

  if (!token) {
    window.location.href = "/pages/login.html";
    return;
  }
  if (role !== expectedRole) {
    window.location.href = role === "ROLE_ADMIN"
      ? "/pages/admin-dashboard.html"
      : "/pages/student-dashboard.html";
  }
}

/**
 * Wraps fetch() with the Authorization header, JSON parsing, and
 * consistent error handling. Every page's JS should call this
 * instead of the raw fetch() API.
 */
async function apiFetch(path, options = {}) {
  const headers = Object.assign({ "Content-Type": "application/json" }, options.headers || {});
  const token = getToken();
  if (token) headers["Authorization"] = "Bearer " + token;

  const res = await fetch(API_BASE + path, Object.assign({}, options, { headers }));

  let body = null;
  try { body = await res.json(); } catch (e) { /* empty body, e.g. some 204s */ }

  if (res.status === 401) {
    clearAuth();
    window.location.href = "/pages/login.html";
    throw new Error("Session expired. Please log in again.");
  }

  if (!res.ok) {
    const message = body && body.message ? body.message : ("Request failed with status " + res.status);
    throw new Error(message);
  }

  return body;
}

function showAlert(elementId, message, type = "error") {
  const el = document.getElementById(elementId);
  if (!el) return;
  el.textContent = message;
  el.className = "alert " + type;
  el.style.display = "block";
}

function hideAlert(elementId) {
  const el = document.getElementById(elementId);
  if (el) el.style.display = "none";
}