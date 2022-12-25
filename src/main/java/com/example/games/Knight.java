package com.example.games;

import static java.lang.Math.abs;  

public class Knight extends Piece{

	public Knight(boolean color) {
		super(color);
		int offset = 8;
		if (color){
			offset = 2;
		}
		setImage(correspondance[offset]);
	}

	@Override
	public boolean validMove(Square start, Square end, Square[][] board) {
		boolean result = true;

		if(abs((end.getCoords()[0]-start.getCoords()[0])*(end.getCoords()[1]-start.getCoords()[1])) == 2) {
			if (end.getContent() != null) {
				if (end.getContent().getColor() == this.getColor()) {
					result = false;
				}
			}
		}
		else {
			result = false;
		}
		return result;
	}
	
}
