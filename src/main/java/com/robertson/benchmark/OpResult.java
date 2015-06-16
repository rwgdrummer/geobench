package com.robertson.benchmark;

public class OpResult
{
	String statName;
	long duration;
	double durationInSecs;
	int count;
	String algorithm;
	Number value;

	public OpResult(
			String statName,
			long duration,
			int count,
			String algorithm,
			Number value ) {
		super();
		this.duration = duration;
		this.statName = statName;
		this.durationInSecs = (double) duration / 1.0e9;
		this.count = count;
		this.algorithm = algorithm;
		this.value = value;
	}

	public String getCSV() {
		return duration + ", " + durationInSecs + ", " + count + ", \"" + algorithm + "\"";
	}

	@Override
	public String toString() {
		return "duration=" + duration + ", durationInSecs=" + durationInSecs + ", count=" + count + ", algorithm=" + algorithm + ", statName=" + statName + ", value=" + value + "";
	}

	public OpResult merge(
			OpResult result ) {
		this.count += result.count;
		this.value = result.value.doubleValue() + this.value.doubleValue();
		this.duration += result.duration;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((algorithm == null) ? 0 : algorithm.hashCode());
		result = prime * result + count;
		result = prime * result + (int) (duration ^ (duration >>> 32));
		result = prime * result + ((statName == null) ? 0 : statName.hashCode());
		return result;
	}

	@Override
	public boolean equals(
			Object obj ) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		OpResult other = (OpResult) obj;
		if (algorithm == null) {
			if (other.algorithm != null) return false;
		}
		else if (!algorithm.equals(other.algorithm)) return false;
		if (count != other.count) return false;
		if (duration != other.duration) return false;
		if (statName == null) {
			if (other.statName != null) return false;
		}
		else if (!statName.equals(other.statName)) return false;
		return true;
	}

}
