(ns animal-form.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [animal-form.events :as events]
   [animal-form.views :as views]
   [animal-form.config :as config]
   [kee-frame.core :as kf]
   ))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn login-page []
  [:div {:style {:width "300px"
                 :height "200px"
                 :position "absolute"
                 :margin "auto"
                 :top 0
                 :bottom 0
                 :left 0
                 :right 0
                 }}
   [:div.field
    [:label.label "用户名"]
    [:div.control
     [:input.input {:type "text" :placeholder "请填写用户名"}]]
    [:label.label "密码"]
    [:div.control
     [:input.input {:type "text" :placeholder "请填写密码"}]]
    [:button.button.is-primary
     {:on-click #(re-frame/dispatch [::events/to-index])}
     "登录"]]])

(defn loading-page []
  [:div "loading"])

(def routes
  [["/" :index]
   ["/login" :login]
   ["/main*path" :main]])

(defn root-component []
  [:div
   [kf/switch-route (fn [route] (get-in route [:data :name]))
    :login [login-page]
    :index [views/main-panel]
    nil [loading-page]]])

(kf/reg-controller
 :xxxx
 {:params (fn [ww]
            (when (-> ww :data :name (= :index))
              true))
  :start  (fn [ctx _]
            (prn "进入start"))})


(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (kf/start!  {:routes         routes
                 :initial-db     {:some-prop true}
                 :root-component [root-component]
                 :debug?         true})
    #_(rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root))
