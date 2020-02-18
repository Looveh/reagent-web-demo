(ns ^:figwheel-hooks reagent-web-demo.core
  (:require [reagent.core :as reagent]
            [cljsjs.react-chartjs-2]))

;; imports

(def line-chart
  (reagent/adapt-react-class js/ReactChartjs2.Line))

;; db

(defonce app-db
  (reagent/atom
    {:date-range ["2019-06-01" "2019-06-02" "2019-06-03" "2019-06-04"]
     :patients   [{:id           :patient-1
                   :dose-history [(rand-int 100) (rand-int 100) (rand-int 100) (rand-int 100)]
                   :color        "rgb(255, 0, 0)"
                   :selected?    true}
                  {:id           :patient-2
                   :dose-history [92 93 95 95]
                   :color        "rgb(0, 0, 255)"
                   :selected?    true}]}))

;; db fns

(defn get-patients [db]
  (:patients db))

(defn get-selected-patients [db]
  (->> db
       (get-patients)
       (filter :selected?)))

(defn get-date-range [db]
  (:date-range db))

(defn toggle-select [patient-id]
  (swap! app-db update :patients
         (fn [patients]
           (mapv (fn [{:keys [id] :as patient}]
                   (if (= id patient-id)
                     (update patient :selected? not)
                     patient))
                 patients))))

;; components

(defn patient-selector
  "Component selects a single patient"
  [{:keys [id color selected?]}]
  [:div {:style {:display        :flex
                 :flex-direction :row
                 :align-items    :center
                 :margin-top     8}}
   [:div {:style {:width            8
                  :height           30
                  :background-color color}}]
   [:input {:style    {:margin 5}
            :type     :checkbox
            :checked  selected?
            :onChange #(toggle-select id)}]
   [:div {:style {:font-family "Arial"
                  :font-weight :lighter}}
    id]])

(defn patient-selectors
  "Component that selects patients"
  []
  (let [patients (get-patients @app-db)]
    [:div {:style {:display         :flex
                   :flex-direction  :column
                   :justify-content :space-around
                   :margin-right    50}}
     [:div {:style {:display        :flex
                    :flex-direction :column}}
      (for [patient patients]
        ^{:key (:id patient)}
        [patient-selector patient])]]))

(defn dose-chart
  "Chart showing the doses"
  []
  (let [date-range        (get-date-range @app-db)
        selected-patients (get-selected-patients @app-db)]
    [:div {:style {:width 500}}
     [line-chart
      {:legend {:display false}
       :data   {:labels   date-range
                :datasets (for [{:keys [id color dose-history]} selected-patients]
                            {:label           id
                             :fill            false
                             :lineTension     0.1
                             :backgroundColor color
                             :borderColor     color
                             :data            dose-history})}}]]))

(defn app-root []
  [:div {:style {:display         :flex
                 :justify-content :space-around
                 :padding-top     50}}
   [:div {:style {:display     :flex
                  :align-items :center}}
    [patient-selectors]
    [dose-chart]]])

;; main

(defn main []
  (reagent/render [app-root] (js/document.getElementById "app")))

(main)
