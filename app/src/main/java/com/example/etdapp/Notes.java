package com.example.etdapp;

class Notes {
    private String label;
    private Double score;

    public Notes(String label, Double score) {
        this.label = label;
        this.score = score;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "label='" + label + '\'' +
                ", score=" + score +
                '}';
    }
}
